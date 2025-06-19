# Franchise API Infrastructure

This directory contains Terraform configurations for deploying the Franchise API on AWS.

## Prerequisites

1. Install [Terraform](https://www.terraform.io/downloads.html) (>= 1.0.0)
2. Configure AWS credentials with appropriate permissions
3. Install [AWS CLI](https://aws.amazon.com/cli/) and configure it with `aws configure`

## Directory Structure

```
infraestructura/terraform/
├── main.tf              # Main Terraform configuration
├── variables.tf          # Input variables
├── outputs.tf            # Output values
├── terraform.tfvars      # Variable definitions (not versioned)
└── modules/
    ├── vpc/             # VPC, subnets, and networking
    ├── ecr/              # ECR repository for Docker images
    ├── ecs/              # ECS cluster and service
    ├── documentdb/       # DocumentDB (MongoDB compatible) cluster
    └── cloudwatch/       # Logging and monitoring
```

## Deployment Steps

1. **Initialize Terraform**
   ```bash
   terraform init
   ```

2. **Review the execution plan**
   ```bash
   terraform plan
   ```

3. **Apply the configuration**
   ```bash
   terraform apply
   ```

4. **Destroy resources** (when no longer needed)
   ```bash
   terraform destroy
   ```

## Variables

Create a `terraform.tfvars` file with the following variables:

```hcl
environment = "dev"
app_name    = "franchise-api"
aws_region = "us-east-1"

db_username = "admin"
db_password = "your-secure-password"

# Optional: Domain configuration
# domain_name      = "api.yourdomain.com"
# hosted_zone_name = "yourdomain.com."
```

## Infrastructure Components

- **VPC** with public and private subnets across multiple AZs
- **ECS Fargate** for container orchestration
- **Application Load Balancer** for traffic routing
- **DocumentDB** (MongoDB compatible) for data storage
- **CloudWatch** for logging and monitoring
- **IAM** roles and policies with least privilege
- **Security Groups** for network security

## Best Practices

- Use workspaces for managing multiple environments (dev/staging/prod)
- Store sensitive values in AWS Parameter Store or Secrets Manager
- Enable versioning on the S3 bucket used for Terraform state
- Use remote state with proper locking
- Regularly update to the latest versions of providers and modules

## License

This project is licensed under the MIT License - see the LICENSE file for details.
