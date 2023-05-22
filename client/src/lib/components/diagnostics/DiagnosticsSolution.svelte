<script>
  export let solution
</script>

<div class="w-[440px] mt-5">
  <span class="block text-sm font-semibold">Solution</span>
  <p class="text-sm">{solution.description}</p>
  <span class="block text-sm font-medium mt-4">Part to replace</span>
  <p class="text-sm">{solution.part.make} - {solution.part.name}</p>
  <p class="text-sm">${solution.part.price}</p>
  <span class="block text-sm font-medium mt-4">Total cost (parts + labor + tax)</span>
  <p class="font-semibold text-sm">${solution.price}</p>
</div>