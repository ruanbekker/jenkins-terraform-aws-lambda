terraform {
  backend "s3" {
    encrypt = true
    bucket = "my-terraform-remote-state-storage-dev"
    key = "terraform/state/s3/python3-layer-lambda.tfstate"
    region = "eu-west-1"
    profile = "dev"
    shared_credentials_file = "/Users/ruan/.aws/credentials"
    dynamodb_table = "terraform-state-lock"
  }
}
