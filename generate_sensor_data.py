#!/usr/bin/env python3

import argparse
from time import time
import random
from itertools import product
import math


def parse_args():
    parser = argparse.ArgumentParser(description='...')
    
    parser.add_argument('--start_time', '-t', type=int, default = int(time())-24*3600,
                        help='time of sensors data start in seconds')

    parser.add_argument('--seed', '-s', type=int,default = random.randint(0,100),
                        help='default seed of random generator for reproduce ability')
    
    parser.add_argument('--points_count', '-p', type=int,default = 3600*24,
                        help='count of points from each sensor')
    
    parser.add_argument('--objects_N', '-N', type=int,default = 3,
                        help='count of objects in data')
    
    parser.add_argument('--sensors_n', '-n', type=int,default = 10,
                    help='count of sensors in data')
    
    parser.add_argument('--output', '-o', type=str, default = './data.json',
                help='default output jsons file')

    return parser.parse_args()

def init_and_shuffle(total_count):
    new_points = list(range(total_count))
    random.shuffle(new_points)
    return new_points

def get_json_for(obj_id,sensor_id,point_time,point_value):
    return '{"objectId": '+str(obj_id)+', "sensorId": '+str(sensor_id)+', "time": '+str(point_time)+', "value": '+str(math.ceil(point_value*100000)/100000)+'}'

if __name__ == "__main__":
    args = parse_args()
    
    random.seed(args.seed)
    
    print('starting generation with parameters: points_count =',args.points_count,
          'sensors_n =',args.sensors_n,
          'objects_N =',args.objects_N,
          'seed =',args.seed)
    
    points = list(range(args.points_count))
    sensors = init_and_shuffle(args.sensors_n)
    objects = init_and_shuffle(args.objects_N)
    
    with open(args.output,'w') as f:
        f.write('[')
        last_object_n = args.points_count*args.sensors_n*args.objects_N-1
        for obj_n,(point_num,sensor_id,obj_id) in enumerate(product(points,sensors,objects)):
            point_time = int(args.start_time + point_num)
            point_value = math.ceil(20*(1*sensor_id)*random.random()-40)
            
            str_to_write = get_json_for(obj_id,sensor_id,point_time,point_value)
            f.write(str_to_write)
            if last_object_n == obj_n:
                f.write(']')
            else:
                f.write(',\n')
    
