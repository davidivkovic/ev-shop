<script>
  import { get } from 'svelte/store'
  import { goto, invalidateAll } from '$app/navigation'
  import { getAllProblems } from '$lib/api/requests'
  import { getShops } from '$lib/api/shops'
  import RequestPreview from '$lib/components/requests/RequestPreview.svelte'
  import RequestRepairDialog from '$lib/components/requests/RequestRepairDialog.svelte'
  import { openDialog } from '$lib/stores/appStore'

  import { isAuthenticated, user, isCustomer } from '$lib/stores/userStore'

  export let data
  $: requests = data ? data.requests : []

  $: !$isAuthenticated && goto('/login')

  $: make = get(user).vehicle?.make
  $: brands = get(user).shop?.brands.join(', ')

  const openRequestRepairDialog = async () => {
    const problems = await getAllProblems()
    const shops = await getShops(make)
    openDialog(RequestRepairDialog, { problems, shops }, async() => await invalidateAll())
  }
</script>

<div class="flex">
  <div>
    <h1 class="text-xl">{isCustomer ? 'View your repair requests' : 'Your repair shop'}</h1>
    <p class="text-sm">
      {isCustomer ? 'Click on a repair request below to see details' : `Repairing ${brands}`}
    </p>
  </div>

  {#if isCustomer}
    <button
      on:click={openRequestRepairDialog}
      class="secondary ml-auto mt-1 h-11 !border-neutral-300 py-1 px-10 !text-sm"
    >
      Request repair
    </button>
  {/if}
</div>

<div class="mt-6 flex flex-col gap-4">
  {#each Object.values(data.requests) as request}
    <RequestPreview {request} />
  {/each}
</div>
