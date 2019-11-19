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
          docker.image('lambci/lambda:build-python3.7').inside('--privileged --user root -e AWS_REGION="eu-west-1"'){
            sh '''echo "START [terraform-step]: start of step"
                bash bin/setup_aws_environment.sh
                export AWS_SHARED_CREDENTIALS_FILE=/tmp/.aws
                echo "pipeline step"
                aws --profile dev s3 ls /
                yum install wget -y
                wget https://releases.hashicorp.com/terraform/0.12.15/terraform_0.12.15_linux_amd64.zip
                unzip terraform_0.12.15_linux_amd64.zip -d /usr/bin
                bash deploy.sh
                echo "END [terraform-step]: end of step"
               '''
          }
        }
      }
    }
  }
}
