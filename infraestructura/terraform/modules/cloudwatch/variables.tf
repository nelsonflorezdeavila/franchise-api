variable "environment" {
  description = "Environment name (e.g., dev, staging, prod)"
  type        = string
}

variable "app_name" {
  description = "Name of the application"
  type        = string
}

variable "ecs_cluster_name" {
  description = "Name of the ECS cluster"
  type        = string
}

variable "ecs_service_name" {
  description = "Name of the ECS service"
  type        = string
}
