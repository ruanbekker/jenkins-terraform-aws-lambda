data "archive_file" "lambda-archive" {
  type        = "zip"
  source_file = "lambda/src/lambda_function.py"
  output_path = "lambda/packages/lambda_function.zip"
}

resource "aws_lambda_function" "lambda-function" {
  filename         = "lambda/packages/lambda_function.zip"
  function_name    = "TerraformManagedLambdaFunction"
  role             = "${aws_iam_role.role_for_lambda.arn}"
  handler          = "lambda_function.lambda_handler"
  source_code_hash = "${data.archive_file.lambda-archive.output_base64sha256}"
  runtime          = "python3.7"
  timeout          = 15
  memory_size      = 128

  layers = ["${aws_lambda_layer_version.python37-requests-layer.arn}"]
}

resource "aws_lambda_layer_version" "python37-requests-layer" {
  filename         = "lambda/packages/python3-requests.zip"
  layer_name       = "python3-requests"
  source_code_hash = "${filebase64sha256("lambda/packages/python3-requests.zip")}"

  compatible_runtimes = ["python3.6", "python3.7"]
}
