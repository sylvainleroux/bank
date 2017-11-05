# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://vagrantcloud.com/search.
  config.vm.box = "ubuntu/trusty64"
  # config.vm.box = "precise64"
   config.vm.box_url = "http://files.vagrantup.com/precise64.box"
   config.vm.network :forwarded_port, guest: 3306, host: 3306
   config.vm.provision :shell, :path => "install-mysql.sh"
   config.vm.synced_folder ".", "/vagrant", :mount_options => ["dmode=777", "fmode=666"]
   config.vm.network "private_network", ip: "33.33.33.10", type: "dhcp", auto_config: false  # SHELL
end
