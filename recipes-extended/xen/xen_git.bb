require xen.inc

#SRCREV = "${AUTOREV}"
SRCREV ?= "3bbc58fd16fc5b08cc2e47f0258694c6b3b4931e"

XEN_REL = "4.12"
XEN_BRANCH = "staging-argo"
FLASK_POLICY_FILE = "xenpolicy-${XEN_REL}-unstable"

PV = "${XEN_REL}+git${SRCPV}"

S = "${WORKDIR}/git"

SRC_URI = " \
    git://github.com/dozylynx/xen;branch=${XEN_BRANCH} \
    "
