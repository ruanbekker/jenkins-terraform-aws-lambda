variable "bucket_name" {
  default = "rb-terraform-state-storage-dev"
}

variable "dynamodb_table" {
  default = "terraform-state-lock"
}
