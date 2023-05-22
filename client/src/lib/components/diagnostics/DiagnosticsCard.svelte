<script>
  import { createEventDispatcher } from "svelte"

  export let measurement
  export let value
  export function trySubmit() {
    console.log('trySubmit')
    form.reportValidity()
    if (form.checkValidity()) {
      dispatch('submit')
    }
  }

  const dispatch = createEventDispatcher()
  let form

</script>

<form bind:this={form} on:submit|preventDefault>
  <p class="text-black mt-10 text-sm mb-1">{measurement.message}</p>
  {#if measurement.type === 'number'}
    <div class="relative">
      <!-- <label for="value">Value</label> -->
      <input required autofocus name="value" type="text" bind:value />
      <span class="absolute right-4 flex inset-y-0 items-center">{measurement.unit}</span>
    </div>
  {:else if measurement.type === 'boolean'}
    <label for="yes" class="cursor-pointer">Yes</label>
    <input required bind:group={value} id="yes" value="yes" type="radio" name="answer" class="cursor-pointer" />
    <label for="no" class="cursor-pointer">No</label>
    <input bind:group={value} id="no" value="no" type="radio" name="answer" class="cursor-pointer"/>
  {/if}
</form>
