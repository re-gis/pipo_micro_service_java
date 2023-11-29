class { 'nginx': }

nginx::resource::vhost{'qa.bippo.com':
  listen_port => 80,
  proxy       => 'http://localhost:8080',
}
