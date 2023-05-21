<script>
  export let measurement
  export let value
</script>

<p>{measurement.message}</p>
{#if measurement.type === 'number'}
  <div class="relative">
    <label for="value">Value</label>
    <input name="value" type="text" bind:value />
    <span class="absolute">{measurement.unit}</span>
  </div>
{:else if measurement.type === 'boolean'}
  <label for="yes">Yes</label>
  <input required bind:group={value} id="yes" value="yes" type="radio" name="answer" />
  <label for="no">No</label>
  <input bind:group={value} id="no" value="no" type="radio" name="answer" />
{/if}
