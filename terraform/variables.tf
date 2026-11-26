variable "aws_region" {
  description = "AWS target region"
  type        = string
  default     = "us-east-1"
}

variable "environment" {
  description = "Target deployment environment"
  type        = string
  default     = "development"
}

variable "app_port" {
  description = "Application HTTP incoming port"
  type        = number
  default     = 8080
}

variable "frontend_bucket_name" {
  description = "Unique S3 bucket name for Angular static website hosting"
  type        = string
  default     = "gestion-station-skii-frontend-728404823665"
}
