# VERY IMPORTANT NOTES:
# 1. This Tiltfile is for a Java project and same Tiltfile can't be used for all sorts of application; some tweaks are required - this Tiltfile is being used for this Java based application: https://github.com/stakater-lab/stakater-nordmart-review
# 2. The Dockerfile must have a specific structure for this Tiltfile to work - for reference look this Dockerfile

# For more on Extensions, see: https://docs.tilt.dev/extensions.html
load('ext://restart_process', 'docker_build_with_restart')
load('ext://helm_resource', 'helm_resource', 'helm_repo')

settings = read_json('tilt_options.json', default={})

# Add Helm repos
helm_repo('stakater', 'https://stakater.github.io/stakater-charts')

if settings.get("namespace"):
  namespace =  settings.get("namespace")

if settings.get("default_registry"):
  default_registry(settings.get("default_registry").format(namespace), host_from_cluster='image-registry.openshift-image-registry.svc:5000/{}'.format(namespace))

if settings.get("allow_k8s_contexts"):
  allow_k8s_contexts(settings.get("allow_k8s_contexts"))

# Watch source code and on change rebuild artifacts and place in target folder
local_resource(
  'review-compile',
  'mvn package && ' +
  ########################################################################################
  # TODO: Update this to match what your pom file produces application-v1-2.0-SNAPSHOT.jar
  ########################################################################################
  'java -Djarmode=layertools -jar target/review-v0.0.0-SNAPSHOT.jar extract --destination target/jar-extracted && ' +
  'rsync --delete --inplace --checksum -r target/jar-extracted/ target/jar',
  deps=['src', 'pom.xml'])

# Keeps the docker image updated
docker_build_with_restart(
  #############################################
  # TODO: Should match following
  # application.applicationName and
  # application.deployment.image.repository
  #############################################
  'review',
  './target/jar',
  entrypoint=['java', 'org.springframework.boot.loader.JarLauncher'],
  platform='linux/amd64',
  #########################################################
  # NOTE: Remember Dockerfile must have a particular format
  #########################################################
  dockerfile='./DockerfileTilt',
  live_update=[
    sync('./target/jar/application', '/app'),
    sync('./target/jar/dependencies', '/app'),
    sync('./target/jar/snapshot-dependencies', '/app'),
    sync('./target/jar/spring-boot-loader', '/app'),
  ])


namespace = "chelsea-dev"
helm_resource('review', './deploy', namespace=namespace, image_deps=["review"], image_keys=[('application.deployment.image.repository', 'application.deployment.image.tag')], flags=['--values=./tilt/values-local.yaml'])

