# CloudWatch Log Group for Application Logs
resource "aws_cloudwatch_log_group" "app" {
  name              = "/ecs/${var.environment}-${var.app_name}"
  retention_in_days = 30
  
  tags = {
    Name        = "${var.environment}-${var.app_name}-logs"
    Environment = var.environment
  }
}

# CloudWatch Log Group for API Gateway
resource "aws_cloudwatch_log_group" "api_gw" {
  name              = "/aws/api-gateway/${var.environment}-${var.app_name}"
  retention_in_days = 30
  
  tags = {
    Name        = "${var.environment}-${var.app_name}-api-gw-logs"
    Environment = var.environment
  }
}

# CloudWatch Alarm for CPU Utilization
resource "aws_cloudwatch_metric_alarm" "cpu_utilization" {
  alarm_name          = "${var.environment}-${var.app_name}-cpu-utilization"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = 2
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = 300
  statistic           = "Average"
  threshold           = 80
  alarm_description   = "This metric monitors ECS CPU utilization"
  
  dimensions = {
    ClusterName = var.ecs_cluster_name
    ServiceName = var.ecs_service_name
  }
  
  alarm_actions = [] # Add SNS topic ARN for notifications
  
  tags = {
    Name        = "${var.environment}-${var.app_name}-cpu-alarm"
    Environment = var.environment
  }
}

# CloudWatch Alarm for Memory Utilization
resource "aws_cloudwatch_metric_alarm" "memory_utilization" {
  alarm_name          = "${var.environment}-${var.app_name}-memory-utilization"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = 2
  metric_name         = "MemoryUtilization"
  namespace           = "AWS/ECS"
  period              = 300
  statistic           = "Average"
  threshold           = 80
  alarm_description   = "This metric monitors ECS memory utilization"
  
  dimensions = {
    ClusterName = var.ecs_cluster_name
    ServiceName = var.ecs_service_name
  }
  
  alarm_actions = [] # Add SNS topic ARN for notifications
  
  tags = {
    Name        = "${var.environment}-${var.app_name}-memory-alarm"
    Environment = var.environment
  }
}

# CloudWatch Dashboard
resource "aws_cloudwatch_dashboard" "main" {
  dashboard_name = "${var.environment}-${var.app_name}-dashboard"
  
  dashboard_body = <<EOF
{
  "widgets": [
    {
      "type": "metric",
      "x": 0,
      "y": 0,
      "width": 12,
      "height": 6,
      "properties": {
        "metrics": [
          ["AWS/ECS", "CPUUtilization", "ServiceName", "${var.ecs_service_name}", "ClusterName", "${var.ecs_cluster_name}"],
          [".", "MemoryUtilization", ".", ".", ".", "."],
          [".", "CPUUtilization", ".", ".", ".", ".", { "stat": "Maximum" }],
          [".", "MemoryUtilization", ".", ".", ".", ".", { "stat": "Maximum" }]
        ],
        "period": 300,
        "stat": "Average",
        "region": "${data.aws_region.current.name}",
        "title": "ECS Service Metrics"
      }
    },
    {
      "type": "log",
      "x": 0,
      "y": 6,
      "width": 24,
      "height": 12,
      "properties": {
        "query": "SOURCE '${aws_cloudwatch_log_group.app.name}' | fields @timestamp, @message\n| sort @timestamp desc\n| limit 100",
        "region": "${data.aws_region.current.name}",
        "title": "Application Logs",
        "view": "table",
        "stacked": false
      }
    }
  ]
}
EOF
}

# Data sources
data "aws_region" "current" {}

# Outputs
output "log_group_name" {
  description = "The name of the CloudWatch Log Group"
  value       = aws_cloudwatch_log_group.app.name
}

output "log_group_arn" {
  description = "The ARN of the CloudWatch Log Group"
  value       = aws_cloudwatch_log_group.app.arn
}
