import subprocess
import sys

args = sys.argv
env = args[1]

command = 'cd ansible && ansible-playbook ordering-service-at-config.yml -i ../../environments/{env}/inventory -vvvv --extra-vars="env={env}"'

subprocess.call(command.format(env=env), shell=True)

