import os
import sys

args = sys.argv
env = args[1]

command = 'cd ansible && ansible-playbook ordering-service-acceptance-config.yml -i ../../environments/{env}/inventory -vvvv --extra-vars="env={env}"'

os.system(command.format(env=env))

