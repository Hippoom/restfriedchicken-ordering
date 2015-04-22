import subprocess
import sys
import os

args = sys.argv
env = args[1]

dir=os.path.dirname(os.path.realpath(__file__))

binary_name=open('{dir}/../build/libs/binary'.format(dir=dir)).read()

command = 'ansible-playbook -i {dir}/../environments/{env}/inventory {dir}/ansible/ordering-service-vm-deployment.yml --extra-vars="ordering_binary={binary_name}"'

subprocess.check_call(command.format(env=env, dir=dir, binary_name=binary_name), shell=True)

