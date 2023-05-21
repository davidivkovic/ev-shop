<script>
  import { goto } from '$app/navigation'
  import { getAllProblems } from '$lib/api/repairs'
  import { getShops } from '$lib/api/shops'
  import RequestRepairDialog from '$lib/components/requests/RequestRepairDialog.svelte'
  import { openDialog } from '$lib/stores/appStore'

  import { isAuthenticated, user } from '$lib/stores/userStore'
  import { get } from 'svelte/store'

  $: !$isAuthenticated && goto('/login')

  $: make = get(user).vehicle.make
  $: customer = get(user).role === 'customer'
  $: brands = get(user).shop?.brands.join(', ')

  const openRequestRepairDialog = async () => {
    const problems = await getAllProblems()
    const shops = await getShops(make)
    openDialog(RequestRepairDialog, { problems, shops })
  }

  
</script>

<div class="flex">
  <div>
    <h1 class="text-xl">{customer ? 'View your repair requests' : 'Your repair shop'}</h1>
    <p class="text-sm">
      {customer ? 'Click on a repair request below to see details' : `Repairing ${brands}`}
    </p>
  </div>

  {#if customer}
    <button
      on:click={openRequestRepairDialog}
      class="secondary ml-auto mt-1 h-11 !border-neutral-300 py-1 px-10 !text-sm"
    >
      Request repair
    </button>
  {/if}
</div>

<!-- here goes requests list -->
