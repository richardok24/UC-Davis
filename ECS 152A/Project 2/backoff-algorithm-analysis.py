# Richard Xie 915505564

# This is a simulation model to simulate a system with N = 10 hosts
# Backoff algorithms can be changed in the run function of class E

import random
import simpy
import math

RANDOM_SEED = 29
SIM_TIME = 1000000
Ts = 1
N_HOSTS = 10;

""" Host class """           
class H:
    def __init__(self, env, arrival_rate):
        self.env = env
        self.arrival_rate = arrival_rate
        # when new pkt comes, reset to 0
        self.L = 0
        self.N = 0
        self.S = 0
        self.server = simpy.Resource(env, capacity = 1)
        env.process (self.packets_arrival(env)) # pkt arriving
                
    def process_packet(self, env):
        self.L -= 1
        # after pkt is successfully transmitted, a new pkt will come
        if (self.L > 0):
            self.N = 0
            self.S = math.floor(env.now) + 1
                                
    def packets_arrival(self, env):
        while True:
            # Infinite loop for generating packets
            yield env.timeout(random.expovariate(self.arrival_rate))
            # wait for next slot if no other pkt
            if (self.L == 0):
                self.N = 0
                self.S = math.floor(env.now) + 1
            self.L += 1


    def exponential_backoff(self, env):
        K = min(self.N, N_HOSTS)
        self.S  = math.floor(env.now) + random.randint(0, 2 ** K) + 1
        self.N += 1


    def linear_backoff(self, env):
        K = min(self.N, 1024)
        self.S  = math.floor(env.now) + random.randint(0, K) + 1
        self.N += 1

""" Ethernet class """	
class E:
    def __init__(self, env, arrival_rate):
        self.env = env
        self.arrival_rate = arrival_rate
        self.hosts = [H(env,arrival_rate) for i in range(N_HOSTS)]
        self.trans = 0
        self.colli = 0

    def run(self, env):
        while True:
            transH = []
            for host in self.hosts:
                # if host wants to transmit
                if ((host.L > 0) and (math.floor(env.now) == host.S)):
                    transH.append(host)
            # host starts transmission if no collision
            if (len(transH) == 1):
                transH[0].process_packet(env)
                self.trans += 1     
            # collision conditions
            if (len(transH) > 1):
                for host in transH:
                    host.exponential_backoff(env)  # backoff algorithm switch
                    self.colli += 1
            # timeout
            yield self.env.timeout(Ts) 
                

def main():
    # output
    print ("{0:<10} {1:<17} {2:<10} {3:<10}".format(
            "Arrival rate","Transmitted pkts", "Throughput", "Collision"))
    random.seed(RANDOM_SEED)
    for arrival_rate in [0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09]:             
        env = simpy.Environment()
        e = E(env, arrival_rate)
        env.process(e.run(env))
        env.run(until = SIM_TIME)
        print ("{0:<12} {1:<17} {2:<10} {3:<10} ".format(
            float(arrival_rate),
            int(e.trans),
            float(float(e.trans) / SIM_TIME),
            int(e.colli)))
        
if __name__ == '__main__': main()