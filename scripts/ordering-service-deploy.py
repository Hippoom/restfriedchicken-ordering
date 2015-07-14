import subprocess
import sys
import os

args = sys.argv
env = args[1]
if len(args) == 3:
    action = args[2]
else:
    action = 'deploy'

if action == 'deploy':
    skip_tags = 'rollback'
else:
    skip_tags = 'deploy'

dir=os.path.dirname(os.path.realpath(__file__))

binary_name=open('{dir}/../build/libs/binary'.format(dir=dir)).read()

workspace=os.path.abspath(os.path.join(os.path.join(os.path.realpath(__file__), os.pardir), os.pardir))

command = 'ansible-playbook -i {dir}/../environments/{env}/inventory {dir}/ansible/ordering-service-deployment.yml --skip-tags "{skip_tags}" --extra-vars="workspace={workspace} env={env} ordering_binary={binary_name}"  -vvvv'

subprocess.check_call(command.format(skip_tags=skip_tags, workspace=workspace, env=env, dir=dir, binary_name=binary_name), shell=True)

