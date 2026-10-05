import pandas as pd
import matplotlib.pyplot as plt

# 1. Task A: Memory vs n
df_mem = pd.read_csv('results/bonus_memory.csv')
plt.figure(figsize=(8, 5))
for s in ['DynamicArray', 'MyLinkedList', 'MinHeap']:
    sub = df_mem[df_mem['structure'] == s].sort_values('n')
    plt.plot(sub['n'], sub['memory_mb'], marker='o', label=s)

plt.xscale('log')
plt.yscale('log')
plt.xlabel('n (Elements)')
plt.ylabel('Memory Footprint (MB)')
plt.title('Task A: Memory Footprint vs n (JOL)')
plt.legend()
plt.grid(True, which="both", ls="--")
plt.savefig('results/plots/memory_vs_n.png')
plt.close()

# 2. Task B: Floyd vs n Inserts
df_heap = pd.read_csv('results/bonus_buildheap.csv')
plt.figure(figsize=(8, 5))
for m in df_heap['method'].unique():
    sub = df_heap[df_heap['method'] == m].sort_values('n')
    label_text = "Floyd O(n)" if m == "floyd_buildHeap" else "n Inserts O(n log n)"
    plt.plot(sub['n'], sub['comparisons'], marker='s', label=f'{label_text} (comparisons)')

plt.xscale('log')
plt.yscale('log')
plt.xlabel('n (Elements)')
plt.ylabel('Comparisons Count')
plt.title('Task B: Floyd buildHeap vs n Inserts (Comparisons)')
plt.legend()
plt.grid(True, which="both", ls="--")
plt.savefig('results/plots/buildheap_comparison.png')
plt.close()

print("Bonus plots regenerated successfully.")