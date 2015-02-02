import os
import sys

args = sys.argv
env = args[1]
if len(args) > 2:
    ordering_service_tag = args[2]
else:
    with open('../build/ordering-service-tag') as ording_service_tag_file:
        print ording_service_tag_file
        ordering_service_tag = ording_service_tag_file.readline()

print ordering_service_tag

command = 'cd ansible && ansible-playbook ordering-service-container.yml -i ../../environments/{env}/inventory -vvvv --extra-vars="env={env} ordering_service_tag={ordering_service_tag}"'

os.system(command.format(env=env, ordering_service_tag=ordering_service_tag))

