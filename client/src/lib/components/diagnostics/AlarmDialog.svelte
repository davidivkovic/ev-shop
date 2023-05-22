<script>
  export let close
</script>

<div class="w-[420px]">
  <h3>Fire Risk!</h3>
  <div class="text-red-900 mt-2 text-">
    <p class="text-red-800 text-sm">The system sensors have detected an increased battery temperature that has lasted longer than 90 seconds. </p>
    <p class="text-red-800 text-sm mt-3">Lithium batteries may erupt in flames if they are exposed to high temperatures for extended periods of time.</p>
    <p class="text-red-800 text-sm mt-3">Please leave the shop immediately and call the fire department.</p>
  </div>
  <button on:click={close} class="bg-red-700 text-white w-full mt-6">Close</button>
</div>