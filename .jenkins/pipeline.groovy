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
          docker.image('ruanbekker/build-tools:v2').inside('-it --entrypoint= --privileged --user root -e AWS_REGION="eu-west-1"'){
          sh '''
             apk update && apk add zip
             export AWS_SHARED_CREDENTIALS_FILE=/tmp/.aws
             cd lambda
             mkdir -p packages/
             mkdir -p layer-requests/python
             pip install virtualenv
             virtualenv .venv -p $(which python)
             source .venv/bin/activate
             pip install -r layer-requests/requirements.txt --no-deps -t lambda/layer-requests/python/
             deactivate .venv
             rm -rf .venv
             zip -r packages/python3-requests.zip lambda/layer-requests/python/
             rm -rf layer-requests/python
             cd ..
             AWS_PROFILE=dev TF_LOG=DEBUG terraform init -upgrade
             terraform apply -auto-approve
             '''
          }
        }
      }
    }
    stage('TerraformStep') {
      steps {
        script {
          docker.image('ruanbekker/build-tools:v2').inside('-it --entrypoint= --privileged --user root -e AWS_REGION="eu-west-1"'){
            sh '''echo "START [terraform-step]: start of step"
                #export AWS_REGION=eu-west-1
                #export AWS_DEFAULT_REGION=eu-west-1
                #export AWS_SHARED_CREDENTIALS_FILE=/tmp/.aws
                #echo "pipeline step"
                #sh bin/setup_aws_environment.sh
                #aws --profile dev s3 ls /
                #wget https://releases.hashicorp.com/terraform/0.12.15/terraform_0.12.15_linux_amd64.zip
                #unzip terraform_0.12.15_linux_amd64.zip -d /tmp
                #sh deploy.sh
                #echo "END [terraform-step]: end of step"
                #rm -rf /tmp/.aws
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
