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
