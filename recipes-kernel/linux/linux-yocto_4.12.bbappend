# openvswitch just plain breaks the boot of xen-guest-image-minimal
# so it needs to go.
# ipv6 conntracking seems unnecessary too, so bin that while we're at it.
KERNEL_MODULE_AUTOLOAD_remove = "nf_conntrack_ipv6 openvswitch"
