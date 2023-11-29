file {['/home/manning/apache-tomcat/webapps/docs',
  '/home/manning/apache-tomcat/webapps/examples',
  '/home/manning/apache-tomcat/webapps/host-manager',
  '/home/manning/apache-tomcat/webapps/manager',
  '/home/manning/apache-tomcat/webapps/ROOT']:
  ensure  => 'absent',
  recurse => true,
  purge   => true,
  force   => true,
  notify => Service['apache-tomcat']
}

file {'/home/manning/apache-tomcat/webapps/ROOT.war':
  ensure => 'present',
  source => '/vagrant/target/web.war',
  owner => 'manning',
  group => 'manning',
  notify => Service['apache-tomcat']
}


file {'/home/manning/apache-tomcat/webapps/api.war':
  ensure => 'present',
  source => '/vagrant/target/api.war',
  owner => 'manning',
  group => 'manning',
  notify => Service['apache-tomcat']
}

file {'/home/manning/apache-tomcat/webapps/dag.war':
  ensure => 'present',
  source => '/vagrant/target/dag.war',
  owner => 'manning',
  group => 'manning',
  notify => Service['apache-tomcat']
}

file {'/home/manning/apache-tomcat/conf/application-qa.properties':
  ensure => 'present',
  source => '/vagrant/files/application-qa.properties',
  owner => 'manning',
  group => 'manning',
  notify => Service['apache-tomcat']
}
