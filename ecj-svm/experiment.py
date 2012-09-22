import subprocess
import argparse

if __name__ == "__main__":
    subprocess.call("java ec.Evolce -file src/ec/app/kernel_gp/kernel_gp.params -p stat.gather-full=true")