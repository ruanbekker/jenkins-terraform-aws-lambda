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

    stage('SetupAwsEnv') {
      steps {
        script {
          sh '''
             sh bin/setup_aws_environment.sh
             '''
        }
      }
    }
    stage('TerraformStep') {
      steps {
        script {
          docker.image('ruanbekker/build-tools:v2').inside('-it --entrypoint= -v /tmp/.aws:/tmp/.aws -e AWS_REGION="eu-west-1"'){
            sh '''echo "START [terraform-step]: start of step"
                echo "env test"
                echo "The slack channel is: ${SLACK_CHANNEL}"
                chown root:root /tmp/.aws
                export AWS_REGION=eu-west-1
                export AWS_DEFAULT_REGION=eu-west-1
                export AWS_SHARED_CREDENTIALS_FILE=/tmp/.aws
                echo "pipeline step"
                aws --profile dev s3 ls /
                wget https://releases.hashicorp.com/terraform/0.12.15/terraform_0.12.15_linux_amd64.zip
                unzip terraform_0.12.15_linux_amd64.zip -d /usr/bin
                sh deploy.sh
                echo "END [terraform-step]: end of step"
               '''
          }
        }
      }
    }
    stage('CleanUp') {
      steps {
        script {
          sh '''
             rm -rf /tmp/.aws
             '''
        }
      }
    }
  }
}
