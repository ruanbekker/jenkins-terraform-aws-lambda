terraform {
  backend "s3" {
    encrypt = true
    bucket = "rb-terraform-remote-state-storage-dev"
    key = "terraform/state/s3/python3-layer-lambda.tfstate"
    region = "eu-west-1"
    dynamodb_table = "terraform-state-lock"
  }
}
