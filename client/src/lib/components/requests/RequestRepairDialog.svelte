<script>
  import { get } from 'svelte/store'

  import { user } from '$lib/stores/userStore'
  import { requestRepair } from '$lib/api/vehicles'
  import { openDialog } from '$lib/stores/appStore'
  import { goto } from '$app/navigation'

  export let close
  export let problems
  export let shops
  let requestForm

  const { make, model, registration } = get(user).vehicle

  const request = async () => {
    try {
      const form = new FormData(requestForm)
      const data = Object.fromEntries(form.entries())
      await requestRepair(data)
      close()
    } catch (e) {
      console.log(e)
    }
  }
</script>

<form bind:this={requestForm} on:submit|preventDefault={request} class="w-[440px]">
  <h3>Request a repair</h3>
  <div class="mt-5 flex w-full items-center gap-10">
    <div class="max-h-[120px] flex-1 overflow-hidden object-top">
      <img
        src={`/images/${make.toLowerCase()}.jpg`}
        alt="Make car"
        class={`h-full w-full ${
          make === 'Audi' || make === 'Kia' ? 'scale-100' : 'scale-150'
        } -mt-3 object-contain object-top`}
      />
    </div>
    <div class="flex-1">
      <div>
        <div class="flex flex-col">
          <span class="text-lg font-semibold">{make}</span>
          <span class=" text-sm">{model}</span>
        </div>
        <span class=" text-sm">{registration}</span>
      </div>
    </div>
  </div>
  <div class="mt-5">
    <label for="problem">Problem</label>
    <select required name="problem">
      <option value="" disabled selected hidden>Select a problem</option>
      {#each problems as problem}
        <option value={problem.type}>{problem.description}</option>
      {/each}
    </select>
  </div>
  <div class="mt-5">
    {#if shops.length === 0}
      <p class="text-[13px]">There are no registered shops for {make}</p>
    {/if}
    {#if shops.length > 0}
      <label class="block" for="shop">Repair shop</label>
      <div class="mt-2">
        {#each shops as shop}
          <div class="flex gap-2">
            <input required type="radio" id={shop.id} name="shop" value={shop.id} />
            <label class="block cursor-pointer" for={shop.id}>{shop.name}, {shop.address}</label>
          </div>
        {/each}
      </div>
    {/if}
  </div>
  <div class="mt-10 flex justify-end gap-4">
    <button class="secondary" type="button" on:click={close()}>Cancel</button>
    <button disabled={shops.length === 0} class="primary" type="submit">Send repair request</button>
  </div>
</form>
