pipeline {
  agent {
    label 'docker'
  }
  
  environment {
    AWS_REGION = "eu-west-1"
    AWS_PROFILE = "dev"
    AWS_DEV_ACCOUNT_NUMBER = credentials('AWS_DEV_ACCOUNT_NUMBER')
    AWS_ACCESS_KEY = credentials('AWS_MASTER_JENKINS_AK')
    AWS_SECRET_KEY = credentials('AWS_MASTER_JENKINS_SK')
    AWS_CROSS_ACCOUNT_ROLE_ARN = "arn:aws:iam::$AWS_DEV_ACCOUNT_NUMBER:role/SystemCrossAccountAccess"
    GIT_TOKEN = credentials('GITHUB_TOKEN')
    SLACK_CHANNEL = "system_events"
    SLACK_TOKEN_SECRET = credentials('SLACK_TOKEN_SECRET')
  }
  
  stages{
    stage('TerraformStep') {
      steps {
        script {
          sh '''echo "START [terraform-step]: start of testing function"
                bash bin/setup_aws_environment.sh
                bash bin/test_function.sh
                echo "END [terraform-step]: end of testing function"
             '''
        }
      }
    }
