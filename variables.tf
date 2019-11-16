variable "bucket_name" {
  default = "my-terraform-state-storage-dev"
}

variable "dynamodb_table" {
  default = "terraform-state-lock"
}
