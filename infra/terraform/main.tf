// vpc
resource "aws_vpc" "cony-vpc" {
  cidr_block = "10.10.0.0/16"
  instance_tenancy = "default"
  enable_dns_support = true
  enable_dns_hostnames = true

  tags = {
    Name = "cony-vpc"
  }
}

// subnet
resource "aws_subnet" "cony-subnet" {
  vpc_id     = aws_vpc.cony-vpc.id
  cidr_block = "10.10.1.0/24"
  availability_zone = "ap-northeast-2a"

  tags = {
    Name = "cony-subnet"
  }
}

// rds subnet
resource "aws_subnet" "cony-subnet-rds" {
  vpc_id = aws_vpc.cony-vpc.id
  cidr_block = "10.10.2.0/24"
  availability_zone = "ap-northeast-2b"
}

// rds subnet group
resource "aws_db_subnet_group" "cony-rds-subnet-group" {
  name = "cony-rds-subnet-group"
  subnet_ids = [aws_subnet.cony-subnet.id, aws_subnet.cony-subnet-rds.id]

  tags = {
    Name = "cony-rds-subnet-group"
  }
}

// igw
resource "aws_internet_gateway" "cony-igw" {
  vpc_id = aws_vpc.cony-vpc.id

  tags = {
    Name = "cony-igw"
  }
}

// route
resource "aws_route_table" "cony-route" {
  vpc_id = aws_vpc.cony-vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.cony-igw.id
  }

  tags = {
    Name = "cony-route"
  }
}

// route and ec2 subnet
resource "aws_route_table_association" "cony-public1" {
  subnet_id = aws_subnet.cony-subnet.id
  route_table_id = aws_route_table.cony-route.id
}

// route and rds subnet
resource "aws_route_table_association" "cony-public2" {
  subnet_id = aws_subnet.cony-subnet-rds.id
  route_table_id = aws_route_table.cony-route.id
}

// 현재 IP 주소 가져오기
data "http" "my_ip" {
  url = "https://checkip.amazonaws.com/"
}

// security group
resource "aws_security_group" "cony-security-group" {
  name = "cony-security-group"
  description = "cony webservice security group"
  vpc_id = aws_vpc.cony-vpc.id

  ingress {
    description = "SSH"
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = ["${chomp(data.http.my_ip.response_body)}/32"]
  }

  ingress {
    from_port = 8080
    to_port = 8080
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 443
    to_port = 443
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # 아웃바운드 규칙 설정
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

// ec2
resource "aws_instance" "cony-ec2" {
  ami                   = "ami-04cb1684c278156a3"
  instance_type         = "t2.micro"
  subnet_id = aws_subnet.cony-subnet.id
  vpc_security_group_ids = [aws_security_group.cony-security-group.id]
  key_name = "primary-key-pair" # 미리 만들어둔 키
  user_data = <<EOF
  #!/bin/bash
  # install jdk17
  sudo yum update -y
  sudo yum install -y java-17-amazon-corretto
  sudo alternatives --set java /usr/lib/jvm/java-17-amazon-corretto.x86_64/bin/java
  java -version
  # 타임존을 서울로 설정
  timedatectl set-timezone Asia/Seoul
  # 호스트네임을 'cony-springboot3-webservice'로 설정
  hostnamectl set-hostname cony-springboot3-webservice
  # /etc/hosts 파일에 새로운 호스트네임 추가
  echo "127.0.0.1   cony-springboot3-webservice" >> /etc/hosts

  EOF

  tags = {
    Name = "cony-springboot3-webservice"
  }
}

// elastic ip (eip)
resource "aws_eip" "cony-eip" {
  instance = aws_instance.cony-ec2.id
}

output "EIP" {
  value = aws_eip.cony-eip.public_ip
}