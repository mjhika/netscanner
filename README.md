# netscanner

This is just a simple network ping sweep utility.

---


## Usage

```shell
clojure -M:scan --cidr "192.168.1.0/24" --timeout 20```

If you don't want to ping a whole range then just use a `/32` as the mask rather
than using something like `/24`


You can shorten the flags with

`-c` and `t` for `--cidr` and `--timeout` respectively

