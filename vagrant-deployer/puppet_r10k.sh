#!/usr/bin/env bash
#
# This installs r10k and pulls the modules for continued installation
#
# We cannot handle failures gracefully here
set -e

if [ "$EUID" -ne "0" ]; then
  echo "This script must be run as root." >&2
  exit 1
fi

if which r10k > /dev/null; then
  echo "r10k already installed"
else
  apt-get update >/dev/null
  echo "Installing ruby 1.9.3..."
  apt-get install -y ruby1.9.1 ruby1.9.1-dev \
  	rubygems1.9.1 irb1.9.1 ri1.9.1 rdoc1.9.1 \
  	build-essential libopenssl-ruby1.9.1 libssl-dev zlib1g-dev

  echo "Installing git..."
  apt-get install -y git

  echo "Installing r10k..."
  gem install r10k -y
fi

echo "Running r10k to fetch modules for puppet provisioner..."
cp /vagrant/puppet/Puppetfile .
r10k puppetfile install
