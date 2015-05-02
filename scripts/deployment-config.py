import subprocess
import sys
import os

args = sys.argv
env = args[1]

dir=os.path.dirname(os.path.realpath(__file__))

command = 'ansible-playbook {dir}/ansible/deployment-config.yml -i {dir}/../environments/{env}/inventory'

subprocess.check_call(command.format(dir=dir, env=env), shell=True)

