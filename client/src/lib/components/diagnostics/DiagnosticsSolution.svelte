<script>
  export let solution
</script>

<div class="w-[440px]">
  <span class="block text-sm font-semibold">Solution</span>
  <p class="text-sm">{solution.description}</p>
  <p class="text-sm">{solution.part.make} - {solution.part.name}</p>
  <p class="text-sm">${solution.part.price}</p>
  <p class="font-semibold text-sm">${solution.price}</p>
</div>