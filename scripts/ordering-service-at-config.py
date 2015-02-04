import subprocess
import sys

args = sys.argv
env = args[1]

command = 'ansible-playbook ansible/ordering-service-at-config.yml -i ../environments/{env}/inventory -vvvv --extra-vars="env={env}"'

subprocess.check_call(command.format(env=env), shell=True)

