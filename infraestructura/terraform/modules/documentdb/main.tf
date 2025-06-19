# DocumentDB Subnet Group
resource "aws_docdb_subnet_group" "main" {
  name        = "${var.environment}-${var.db_name}-subnet-group"
  description = "Allowed subnets for DocumentDB cluster"
  subnet_ids  = var.private_subnet_ids
  
  tags = {
    Name = "${var.environment}-${var.db_name}-subnet-group"
  }
}

# DocumentDB Parameter Group
resource "aws_docdb_cluster_parameter_group" "main" {
  family      = "docdb4.0"
  name        = "${var.environment}-${var.db_name}-parameter-group"
  description = "DocumentDB cluster parameter group"
  
  parameter {
    name  = "tls"
    value = "disabled"
  }
  
  tags = {
    Name = "${var.environment}-${var.db_name}-parameter-group"
  }
}

# DocumentDB Cluster
resource "aws_docdb_cluster" "main" {
  cluster_identifier      = "${var.environment}-${var.db_name}-cluster"
  engine                  = "docdb"
  master_username         = var.master_username
  master_password         = var.master_password
  backup_retention_period = 7
  preferred_backup_window = "07:00-09:00"
  skip_final_snapshot     = true
  db_subnet_group_name    = aws_docdb_subnet_group.main.name
  vpc_security_group_ids  = [aws_security_group.main.id]
  
  db_cluster_parameter_group_name = aws_docdb_cluster_parameter_group.main.name
  
  tags = {
    Name = "${var.environment}-${var.db_name}-cluster"
  }
}

# DocumentDB Cluster Instances
resource "aws_docdb_cluster_instance" "main" {
  count              = var.instance_count
  identifier         = "${var.environment}-${var.db_name}-instance-${count.index + 1}"
  cluster_identifier = aws_docdb_cluster.main.id
  instance_class     = var.instance_class
  
  tags = {
    Name = "${var.environment}-${var.db_name}-instance-${count.index + 1}"
  }
}

# Security Group for DocumentDB
resource "aws_security_group" "main" {
  name        = "${var.environment}-${var.db_name}-sg"
  description = "Security group for DocumentDB cluster"
  vpc_id      = var.vpc_id
  
  ingress {
    from_port   = 27017
    to_port     = 27017
    protocol    = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }
  
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  tags = {
    Name = "${var.environment}-${var.db_name}-sg"
  }
}

# Outputs
output "endpoint" {
  description = "The DNS address of the DocumentDB instance"
  value       = aws_docdb_cluster.main.endpoint
}

output "port" {
  description = "The port the DocumentDB instance listens on"
  value       = aws_docdb_cluster.main.port
}

output "master_username" {
  description = "The master username for the DocumentDB cluster"
  value       = aws_docdb_cluster.main.master_username
  sensitive   = true
}

output "security_group_id" {
  description = "The security group ID of the DocumentDB cluster"
  value       = aws_security_group.main.id
}
