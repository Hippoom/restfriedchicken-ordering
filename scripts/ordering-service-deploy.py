import subprocess
import sys
import os

args = sys.argv
env = args[1]

dir=os.path.dirname(os.path.realpath(__file__))

binary_name=open('{dir}/../build/libs/binary'.format(dir=dir)).read()

workspace=os.path.abspath(os.path.join(os.path.join(os.path.realpath(__file__), os.pardir), os.pardir))

command = 'ansible-playbook -i {dir}/../environments/{env}/inventory {dir}/ansible/ordering-service-vm-deployment.yml --extra-vars="workspace={workspace} env={env} ordering_binary={binary_name}"'

subprocess.check_call(command.format(workspace=workspace, env=env, dir=dir, binary_name=binary_name), shell=True)

