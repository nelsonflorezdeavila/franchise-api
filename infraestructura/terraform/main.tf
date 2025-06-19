terraform {
  required_version = ">= 1.0.0"
  
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
  
  backend "s3" {
    # This will be configured when initializing the backend
    # bucket = "your-terraform-state-bucket"
    # key    = "franchise-api/terraform.tfstate"
    # region = "us-east-1"
  }
}

provider "aws" {
  region = var.aws_region
  
  default_tags {
    tags = {
      Environment = var.environment
      Project     = "franchise-api"
      ManagedBy   = "terraform"
    }
  }
}

# Get the current AWS account ID
# data "aws_caller_identity" "current" {}

# Get the current AWS region
# data "aws_region" "current" {}

# VPC Module
module "vpc" {
  source = "./modules/vpc"
  
  environment = var.environment
  vpc_cidr    = var.vpc_cidr
  
  public_subnets  = var.public_subnets
  private_subnets = var.private_subnets
  
  availability_zones = var.availability_zones
}

# ECR Repository
module "ecr" {
  source = "./modules/ecr"
  
  environment = var.environment
  app_name    = var.app_name
}

# ECS Cluster
module "ecs" {
  source = "./modules/ecs"
  
  environment = var.environment
  app_name    = var.app_name
  
  vpc_id             = module.vpc.vpc_id
  private_subnet_ids  = module.vpc.private_subnet_ids
  public_subnet_ids   = module.vpc.public_subnet_ids
  
  container_port     = 8080
  container_cpu       = 256
  container_memory    = 512
  desired_count       = 2
  
  ecr_repository_url = module.ecr.repository_url
  
  # Database connection variables
  db_username        = var.db_username
  db_password        = var.db_password
  documentdb_endpoint = module.documentdb.endpoint
  
  depends_on = [module.vpc, module.ecr, module.documentdb]
}

# DocumentDB (MongoDB compatible) - Using DocumentDB since MongoDB Atlas is a separate service
module "documentdb" {
  source = "./modules/documentdb"
  
  environment = var.environment
  vpc_id      = module.vpc.vpc_id
  
  db_name          = "franchisedb"
  master_username   = var.db_username
  master_password   = var.db_password
  instance_class    = "db.t3.medium"
  instance_count    = 1
  
  private_subnet_ids = module.vpc.private_subnet_ids
  vpc_cidr           = var.vpc_cidr
  
  depends_on = [module.vpc]
}

# CloudWatch Logs
module "logs" {
  source = "./modules/cloudwatch"
  
  environment      = var.environment
  app_name         = var.app_name
  ecs_cluster_name = module.ecs.cluster_name
  ecs_service_name = module.ecs.service_name
  
  depends_on = [module.ecs]
}

# Outputs are defined in outputs.tf
