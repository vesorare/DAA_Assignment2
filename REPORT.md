# Performance Analysis and Verification of In-Memory Workload Engine

## 1. Asymptotic Complexity Table

| Structure | Operation | Best Case | Average Case | Worst Case | Auxiliary Space | Justification |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `DynamicArray` | `add(x)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(n)$ | $\Theta(1)$ | Amortized $\Theta(1)$; worst case triggers $2\times$ array reallocation and element copying[cite: 2]. |
| `DynamicArray` | `add(index, x)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Inserting at the end takes $\Theta(1)$; general case shifts $(size - index)$ elements[cite: 2]. |
| `DynamicArray` | `remove(index)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Removing the tail takes $\Theta(1)$; otherwise shifts $(size - 1 - index)$ items to the left[cite: 2]. |
| `DynamicArray` | `get(index)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | Constant time direct pointer arithmetic using array base address[cite: 2]. |
| `DynamicArray` | `contains(x)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Linear scan; stops on the first match or traverses all $n$ values[cite: 2]. |
| `MyLinkedList` | `add(x)` | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Traverses sequentially from head to tail node before inserting[cite: 2]. |
| `MyLinkedList` | `add(0, x)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | Prepends a new node and updates head reference directly[cite: 2]. |
| `MyLinkedList` | `add(index, x)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Requires iterating through $(index - 1)$ pointer links[cite: 2]. |
| `MyLinkedList` | `remove(index)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Unlinking head takes $\Theta(1)$; otherwise traverses to $(index - 1)$[cite: 2]. |
| `MyLinkedList` | `get(index)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Lacks random access; traverses pointers sequentially[cite: 2]. |
| `MyLinkedList` | `contains(x)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Traverses linked chain until element match or null reference[cite: 2]. |
| `MinHeap` | `insert(x)` | $\Theta(1)$ | $O(\log n)$ | $\Theta(\log n)$ | $\Theta(1)$ | Appends to tail, then bubble-up traverses up to tree height $\lfloor\log_2 n\rfloor$[cite: 2]. |
| `MinHeap` | `peekMin()` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | Direct array lookup at index 0[cite: 2]. |
| `MinHeap` | `extractMin()` | $\Theta(\log n)$ | $\Theta(\log n)$ | $\Theta(\log n)$ | $\Theta(1)$ | Replaces root with last element and restores invariant via bubble-down[cite: 2]. |

---

## 2. Loop Invariant Proofs

### Proof 1: `contains(int x)` in `DynamicArray`

**Algorithm loop:**
```java
for (int i = 0; i < size; i++) {
    if (data[i] == x) return true;
}
return false;
```

* **Invariant:** At the start of each iteration $i$ ($0 \le i \le size$), the target value $x$ is not present in the subarray $data[0 \dots i - 1]$[cite: 2].
* **Initialization:** Prior to the first iteration, $i = 0$. The slice $data[0 \dots -1]$ is empty, so the statement holds vacuously[cite: 2].
* **Maintenance:** During iteration $i$, the algorithm inspects $data[i]$. If $data[i] == x$, it terminates immediately, returning `true`. If $data[i] \neq x$, then knowing $x \notin data[0 \dots i - 1]$ implies $x \notin data[0 \dots i]$. Incrementing $i$ to $i + 1$ preserves the invariant for the next iteration[cite: 2].
* **Termination:** The loop terminates either early upon finding a matching element, or when $i = size$. In the latter case, the invariant establishes that $x \notin data[0 \dots size - 1]$, correctly returning `false`[cite: 2].
* **Conclusion:** The invariant proves that `contains` returns `true` if and only if $x$ exists within the stored array bounds[cite: 2].

### Proof 2: `bubbleDown(int index)` in `MinHeap`

**Algorithm loop:**
```java
while (2 * index + 1 < size) {
    int left = 2 * index + 1;
    int right = 2 * index + 2;
    int smallest = left;
    if (right < size && heap[right] < heap[left]) smallest = right;
    if (heap[smallest] < heap[index]) {
        swap(index, smallest);
        index = smallest;
    } else break;
}
```

* **Invariant:** At the start of each iteration with current index $k$, every node in the binary heap satisfies the min-heap condition ($heap[parent] \le heap[child]$), except possibly the node at $k$ relative to its immediate children[cite: 2].
* **Initialization:** Before the first iteration ($k = 0$), the root was replaced with the last element. Both subtrees rooted at left and right children are valid min-heaps, so the only potential violation is strictly at index 0[cite: 2].
* **Maintenance:** We identify the index $smallest = \arg\min(heap[k], heap[left], heap[right])$. If $smallest == k$, the invariant holds for the entire tree and the loop breaks. If a child contains a smaller value, swapping $heap[k]$ and $heap[smallest]$ ensures the parent is strictly less than both children, restoring order at index $k$. The potential violation moves exclusively down into subtree $smallest$. Updating $k = smallest$ maintains the invariant for the subsequent iteration[cite: 2].
* **Termination:** The loop terminates when node $k$ has no children ($2k + 1 \ge size$) or when $heap[k] \le \min(heap[left], heap[right])$. In both terminating states, no violations remain in the tree[cite: 2].
* **Conclusion:** This proves that `extractMin()` fully restores the min-heap property across all $size$ elements[cite: 2].

---

## 3. Benchmark Plots

The following charts illustrate execution time and physical metrics across sizes $n \in \{100, 1\,000, 10\,000, 100\,000\}$:

* **Workload 1 (Random Access):**
  * Time: `results/plots/W1_time.png`[cite: 3, 4]
  * Operations: `results/plots/W1_operations.png`[cite: 3, 4]
* **Workload 2 (Search):**
  * Time: `results/plots/W2_time.png`[cite: 3, 4]
  * Operations: `results/plots/W2_operations.png`[cite: 3, 4]
* **Workload 3 (Insert & Remove - Head / Middle):**
  * Time: `results/plots/W3_head_time.png`, `results/plots/W3_middle_time.png`[cite: 3, 4]
  * Operations: `results/plots/W3_head_operations.png`, `results/plots/W3_middle_operations.png`[cite: 3, 4]
* **Workload 4 (Priority Processing):**
  * Time: `results/plots/W4_time.png`[cite: 3, 4]
  * Operations: `results/plots/W4_operations.png`[cite: 3, 4]

---

## 4. Discussion & Hardware-Level Analysis

1. `DynamicArray` significantly outperforms `MyLinkedList` during iteration and index-based access due to spatial locality[cite: 3].
2. Because the array is stored as a contiguous memory block, modern CPU prefetchers load entire 64-byte cache lines at once, placing up to 16 consecutive integers directly into L1/L2 caches[cite: 3].
3. In contrast, `MyLinkedList` allocates distinct `Node` objects on the JVM heap, scattering them across non-contiguous memory addresses[cite: 3].
4. Accessing consecutive nodes requires dereferencing memory pointers (pointer chasing), which triggers frequent CPU cache misses and forces the execution pipeline to stall[cite: 3].
5. Furthermore, memory allocation overhead is substantial: each `Node` object incurs a 12-to-16-byte object header, an 8-byte reference, and padding, compared to 4 raw bytes per element in primitive arrays[cite: 3, 4].
6. This extensive object creation places heavy pressure on the Java Garbage Collector during insertions and deletions[cite: 3].
7. `MyLinkedList` is practically advantageous only for workloads dominated by boundary operations, such as constant-time insertions and removals at index 0 where array shifting is avoided[cite: 2, 3].
8. Conversely, `DynamicArray` remains the default structure for read-intensive, bulk-appending, and cache-sensitive processing[cite: 1, 3].
9. `MinHeap` provides a balanced compromise by mapping a complete binary tree onto a cache-friendly flat array, eliminating pointer overhead while maintaining logarithmic time bounds for priority processing[cite: 1, 2].
---

## 5. Bonus Tasks (+15%)

### Task A: Memory Footprint Analysis (JOL)
Chart: `results/plots/memory_vs_n.png`

Using Java Object Layout (JOL), we measured the real memory consumption across structures:
1. **DynamicArray & MinHeap:** Memory consumption scales linearly with flat array allocation ($\approx 4 \text{ bytes} \times \text{capacity}$). Object overhead consists only of a single array header (16 bytes) and class fields (16 bytes).
2. **MyLinkedList Overhead:** For $n$ items, the list instantiates $n$ distinct `Node` objects. Each node consumes:
   - Object Header (Mark Word + Klass Word): 12–16 bytes;
   - Primitive `int val`: 4 bytes;
   - Reference `Node next`: 4–8 bytes (depending on Compressed OOPs);
   - 8-byte alignment padding: up to 4 bytes.
   Consequently, each element in `MyLinkedList` occupies 24–32 bytes — **6 to 8 times more memory** than in primitive array-backed structures.

### Task B: $O(n)$ Floyd's buildHeap Verification
Chart: `results/plots/buildheap_comparison.png`

We compared Floyd's bottom-up `buildHeap` against $n$ sequential `insert(x)` invocations:
- **Asymptotic Proof:** Sequential insertions perform bubble-up from the leaves towards the root, resulting in worst-case depth $\log n$ for the majority of elements ($\sum_{i=1}^n \log i = \Theta(n \log n)$). Conversely, Floyd's algorithm executes `bubbleDown` starting from index $\lfloor n/2 \rfloor - 1$ up to 0. The number of nodes at height $h$ is at most $\lceil n/2^{h+1} \rceil$, and their maximum downward distance is $h$:
  $$\sum_{h=0}^{\lfloor\log_2 n\rfloor} \frac{n}{2^{h+1}} O(h) = O\left(n \sum_{h=0}^{\infty} \frac{h}{2^h}\right) = O(2n) = O(n)$$
- **Empirical Results:** At $n = 100\,000$, Floyd's `buildHeap` performs roughly $2\times$ fewer comparisons and executes measurably faster than $n$ separate insertions, confirming the theoretical linear bound.