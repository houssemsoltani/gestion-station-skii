param(
    [string]$BucketName = "gestion-station-skii-frontend-728404823665",
    [string]$Region = "us-east-1"
)

$ErrorActionPreference = "Stop"
$ProjectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$FrontendDir = Join-Path $ProjectRoot "frontend-gestion-station-skii"
$DistDir = Join-Path $FrontendDir "dist\frontend-gestion-station-skii\browser"

Write-Host "1. Build Angular (production)..." -ForegroundColor Cyan
Push-Location $FrontendDir
npm run build -- --configuration=production
Pop-Location

Write-Host "2. Build et lancement Docker du frontend (port 4200)..." -ForegroundColor Cyan
docker build -t gestion-station-skii-frontend:latest $FrontendDir
docker rm -f frontend-station-skii 2>$null | Out-Null
docker run -d --name frontend-station-skii --restart unless-stopped -p 4200:80 gestion-station-skii-frontend:latest

Write-Host "3. Creation / Configuration du bucket AWS S3 ($BucketName)..." -ForegroundColor Cyan
if (-not $env:AWS_ACCESS_KEY_ID -or -not $env:AWS_SECRET_ACCESS_KEY) {
    Write-Host "ERREUR: Variables AWS_ACCESS_KEY_ID et AWS_SECRET_ACCESS_KEY manquantes dans PowerShell." -ForegroundColor Red
    Write-Host "Exemple: `$env:AWS_ACCESS_KEY_ID='...'; `$env:AWS_SECRET_ACCESS_KEY='...'; `$env:AWS_SESSION_TOKEN='...'" -ForegroundColor Yellow
    exit 1
}

$policyJson = @"
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::$BucketName/*"
    }
  ]
}
"@

$AwsDockerArgs = @(
    "run", "--rm",
    "-e", "AWS_ACCESS_KEY_ID=$env:AWS_ACCESS_KEY_ID",
    "-e", "AWS_SECRET_ACCESS_KEY=$env:AWS_SECRET_ACCESS_KEY",
    "-e", "AWS_DEFAULT_REGION=$Region"
)
if ($env:AWS_SESSION_TOKEN) {
    $AwsDockerArgs += @("-e", "AWS_SESSION_TOKEN=$env:AWS_SESSION_TOKEN")
}

# Creation du bucket (ignore si deja existant)
$TempPolicyDir = Join-Path $env:TEMP "s3-policy-$([guid]::NewGuid().ToString('N'))"
New-Item -ItemType Directory -Path $TempPolicyDir -Force | Out-Null
$PolicyFile = Join-Path $TempPolicyDir "policy.json"
[System.IO.File]::WriteAllText($PolicyFile, $policyJson, (New-Object System.Text.UTF8Encoding($false)))

try {
    $ErrorActionPreference = "Continue"
    & docker @AwsDockerArgs amazon/aws-cli s3api head-bucket --bucket $BucketName 2>$null | Out-Null
    $bucketExists = ($LASTEXITCODE -eq 0)
    $ErrorActionPreference = "Stop"
    if (-not $bucketExists) {
        & docker @AwsDockerArgs amazon/aws-cli s3 mb "s3://$BucketName"
    }
    & docker @AwsDockerArgs amazon/aws-cli s3api put-public-access-block --bucket $BucketName --public-access-block-configuration "BlockPublicAcls=false,IgnorePublicAcls=false,BlockPublicPolicy=false,RestrictPublicBuckets=false"
    $PolicyArgs = $AwsDockerArgs + @("-v", "${TempPolicyDir}:/policy", "amazon/aws-cli", "s3api", "put-bucket-policy", "--bucket", $BucketName, "--policy", "file:///policy/policy.json")
    & docker @PolicyArgs
    & docker @AwsDockerArgs amazon/aws-cli s3 website "s3://$BucketName/" --index-document index.html --error-document index.html

    Write-Host "4. Upload des fichiers Angular vers s3://$BucketName..." -ForegroundColor Cyan
    $SyncArgs = $AwsDockerArgs + @("-v", "${DistDir}:/dist", "amazon/aws-cli", "s3", "sync", "/dist", "s3://$BucketName", "--delete")
    & docker @SyncArgs
} finally {
    Remove-Item -Path $TempPolicyDir -Recurse -Force -ErrorAction SilentlyContinue
}

Write-Host ""
Write-Host "=== DEPLOIEMENT TERMINE ===" -ForegroundColor Green
Write-Host "Docker local : http://localhost:4200"
Write-Host "AWS S3 URL   : http://$BucketName.s3-website-$Region.amazonaws.com"
