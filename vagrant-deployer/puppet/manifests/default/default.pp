class { 'jdk_oracle':}

user {'manning':
  ensure     => 'present',
  managehome => true
}

$dirname = 'apache-tomcat-8.0.33'
$filename = "${dirname}.zip"
$install_path = "/home/manning/${dirname}"

file { '/home/manning/apache-tomcat':
  ensure  => 'link',
  target  => $install_path,
  owner   => 'manning',
  group   => 'manning',
  require => [Archive[$filename], User['manning']]
}

archive { $filename:
  path          => "/tmp/${filename}",
  source        => "http://mirrors.sonic.net/apache/tomcat/tomcat-8/v8.0.33/bin/apache-tomcat-8.0.33.zip",
  checksum      => 'c99b581878e8cfc7337ebdd84dbde615b9a07eb0',
  checksum_type => 'sha1',
  extract       => true,
  extract_path  => '/home/manning',
  creates       => $install_path,
  cleanup       => 'true',
  require       => [Package['unzip'],User['manning']]
}

exec { 'tomcat permission':
  command   => "chown -R manning:manning $install_path",
  path      => $::path,
  subscribe => Archive[$filename],
}

exec {'tomcat executable':
  command => "chmod +x /home/manning/apache-tomcat/bin/*.sh",
  path    => $::path,
  require => File['/home/manning/apache-tomcat']
}

file {'/etc/init/apache-tomcat.conf':
  source => '/vagrant/files/apache-tomcat.conf',
  ensure => 'present'
}

service {'apache-tomcat':
  enable  => true,
  ensure  => 'running',
  require => File['/etc/init/apache-tomcat.conf']
}
