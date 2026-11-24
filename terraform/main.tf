resource "aws_security_group" "ski_app_sg" {
  name        = "gestion-station-skii-sg"
  description = "Security Group autorisant le trafic HTTP entrant sur le port 8080"

  ingress {
    description = "HTTP application access"
    from_port   = var.app_port
    to_port     = var.app_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "All outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name        = "gestion-station-skii-sg"
    Environment = var.environment
  }
}
