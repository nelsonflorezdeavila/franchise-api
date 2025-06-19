variable "environment" {
  description = "Environment name (e.g., dev, staging, prod)"
  type        = string
}

variable "db_name" {
  description = "Name of the database"
  type        = string
}

variable "master_username" {
  description = "Master username for the DocumentDB cluster"
  type        = string
  sensitive   = true
}

variable "master_password" {
  description = "Master password for the DocumentDB cluster"
  type        = string
  sensitive   = true
}

variable "instance_class" {
  description = "Instance class for DocumentDB instances"
  type        = string
  default     = "db.t3.medium"
}

variable "instance_count" {
  description = "Number of DocumentDB instances to create"
  type        = number
  default     = 1
}

variable "vpc_id" {
  description = "VPC ID where the DocumentDB cluster will be created"
  type        = string
}

variable "private_subnet_ids" {
  description = "List of private subnet IDs for the DocumentDB subnet group"
  type        = list(string)
}

variable "vpc_cidr" {
  description = "CIDR block of the VPC for security group rules"
  type        = string
}
