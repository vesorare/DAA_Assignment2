import os
import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv('results/results.csv')
os.makedirs('results/plots', exist_ok=True)

workloads = df['workload'].unique()

for w in workloads:
    sub = df[df['workload'] == w]
    variants = sub['variant'].unique()
    
    for v in variants:
        v_sub = sub[sub['variant'] == v]
        title_suffix = f" ({v})" if v != "-" else ""
        file_suffix = f"_{v}" if v != "-" else ""
        
        # 1. График Time vs n
        plt.figure(figsize=(8, 5))
        for struct in v_sub['structure'].unique():
            data = v_sub[v_sub['structure'] == struct]
            plt.plot(data['n'], data['time_ms'], marker='o', label=struct)
        plt.xscale('log')
        plt.yscale('log')
        plt.xlabel('n (Elements)')
        plt.ylabel('Time (ms)')
        plt.title(f'{w}{title_suffix} - Time vs n')
        plt.legend()
        plt.grid(True, which="both", ls="--")
        plt.savefig(f'results/plots/{w}{file_suffix}_time.png')
        plt.close()

        # 2. График Operations vs n
        plt.figure(figsize=(8, 5))
        for struct in v_sub['structure'].unique():
            data = v_sub[v_sub['structure'] == struct]
            plt.plot(data['n'], data['steps'], marker='s', label=f'{struct} steps')
            plt.plot(data['n'], data['moves'], marker='^', linestyle=':', label=f'{struct} moves')
            plt.plot(data['n'], data['comparisons'], marker='x', linestyle='--', label=f'{struct} comparisons')
        plt.xscale('log')
        plt.yscale('log')
        plt.xlabel('n (Elements)')
        plt.ylabel('Operations Count')
        plt.title(f'{w}{title_suffix} - Operations vs n')
        plt.legend()
        plt.grid(True, which="both", ls="--")
        plt.savefig(f'results/plots/{w}{file_suffix}_operations.png')
        plt.close()

print("All plots generated successfully in results/plots/")