import os
import sys

args = sys.argv
env = args[1]
if len(args) > 2:
    ordering_service_tag = args[2]
else:
    ordering_service_tag = 'latest'

print ordering_service_tag

command = 'cd ansible && ansible-playbook ordering-service-image.yml -i ../../environments/{env}/inventory -vvvv --extra-vars="ordering_service_tag={ordering_service_tag}"'

os.system(command.format(env=env, ordering_service_tag=ordering_service_tag))
os.system('mkdir -p ../build && touch ../build/ordering-service-tag')

with open('../build/ordering-service-tag', "w") as the_file:
    the_file.write(ordering_service_tag)