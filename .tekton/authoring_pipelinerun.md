To create a pipeline, the delivery engineer first needs to perform a few steps:

First, we need to create a service account in the namespace in which our pipeline will run.
The name of this service account needs to 'pipeline'.
We also need to add a secret, nexus-docker-config to this service account. We will use this secret later in the buildah task in our pipeline.

Here is the CRD for our service account:

```yaml
kind: ServiceAccount
apiVersion: v1
metadata:
  name: pipeline
  namespace: <tenant>-build
secrets:
  - name: nexus-docker-config
```
Next we add this SCC:
```yaml
apiVersion: security.openshift.io/v1
kind: SecurityContextConstraints
allowHostPorts: false
priority: 10
requiredDropCapabilities:
  - MKNOD
allowPrivilegedContainer: true
runAsUser:
  type: RunAsAny
users: []
allowHostDirVolumePlugin: false
allowHostIPC: false
seLinuxContext:
  type: MustRunAs
readOnlyRootFilesystem: false
metadata:
  name: tekton-pipelines-scc
fsGroup:
  type: MustRunAs
groups: []
defaultAddCapabilities: null
supplementalGroups:
  type: RunAsAny
volumes:
  - configMap
  - downwardAPI
  - emptyDir
  - persistentVolumeClaim
  - projected
  - secret
allowHostPID: false
allowHostNetwork: false
allowPrivilegeEscalation: true
allowedCapabilities: null
```
![SCC](images/allowpriv.png)
Now we will create a role and rolebinding for this service account so it can use this SCC.
```yaml
kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: pipeline-rolbinding-for-nordmart
  namespace: <tenant>-build

subjects:
  - kind: ServiceAccount
    name: pipeline
    namespace: <tenant>-build
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: pipelines-scc-role-for-nordmart
```

```yaml
kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: pipelines-scc-role
rules:
  - verbs:
      - use
    apiGroups:
      - security.openshift.io
    resources:
      - securitycontextconstraints
    resourceNames:
      - tekton-pipelines-scc
```
Make sure allow privileged is set to true as we need this for the buildah task.




