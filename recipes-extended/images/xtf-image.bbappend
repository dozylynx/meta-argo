WKS_FILE:qemux86-64 = "qemuboot-xen-argo-x86-64.wks"
WKS_FILES:qemux86-64:append = " qemuboot-xen-argo-x86-64.wks"

TEST_SUITES = "ping ssh xtf_minimal"

do_testimage[depends] += " xtf-image:do_build"
