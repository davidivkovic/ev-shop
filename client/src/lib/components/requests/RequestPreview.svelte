<script>
  import dayjs from 'dayjs'
  import { invalidateAll } from '$app/navigation'
  import ClockIcon from '~icons/tabler/clock-filled'
  import CheckIcon from '~icons/tabler/circle-check-filled'
  import XIcon from '~icons/tabler/circle-x-filled'

  import { isCustomer } from '$lib/stores/userStore'
  import { openDialog } from '$lib/stores/appStore'
  import RequestProcessDialog from './RequestProcessDialog.svelte'
  import DiagnosisDialog from '../diagnostics/DiagnosisDialog.svelte'

  import { resetDiagnostics } from '$lib/api/requests'

  export let request

  const { make, model, registration } = request.vehicle

  $: requestStatus = request.accepted ? 'Accepted' : request.rejected ? 'Rejected' : 'Pending'

  const openProcessDialog = () => {
    openDialog(RequestProcessDialog, { requestId: request.id }, async () => await invalidateAll())
  }

  const openDiagnosticDialog = () => {
    openDialog(DiagnosisDialog, {request}, async result => {
      if (result === 'ok') await invalidateAll()
      else await resetDiagnostics(request.id)
    })
  }
</script>

<div class="h-30 w-full rounded-lg border border-neutral-300 bg-white p-5 pr-8">
  <div class="flex items-center justify-between">
    <div class="flex items-center gap-10">
      <div class="flex items-center">
        <img
          src={`images/${make.toLowerCase()}.jpg`}
          alt="Make car"
          class="-ml-4 h-20 w-40 object-contain"
        />
        <div>
          <span class="block font-medium">{make}</span>
          <span class="block text-[13px]">{model}</span>
          <span class="block text-[13px]">{registration}</span>
        </div>
      </div>

      <div class="text-left">
        <span class="block text-[13px] font-semibold">Reported problem</span>
        <span class="text-[13px]">{request.problem.description}</span>
        {#if request.scheduledAt !== null}
          <div class="text-[13px]">
            Scheduled at {dayjs(request.scheduledAt).format('MMMM D, YYYY h:mm A')}
          </div>
        {/if}
      </div>
    </div>

    <div class="flex items-center gap-1.5">
      {#if (requestStatus !== 'Pending' && requestStatus !== 'Accepted') || $isCustomer}
        <span class="text-[13px] font-medium">{requestStatus}</span>
      {/if}
      {#if requestStatus === 'Pending'}
        {#if $isCustomer}
          <ClockIcon class="h-4 w-4 text-amber-500" />
        {:else}
          <button on:click={openProcessDialog} class="secondary !text-[13px] !py-2 !px-[22px]">Process</button>
        {/if}
      {:else if requestStatus === 'Accepted'}
        {#if $isCustomer}
          <CheckIcon class="h-4 w-4" />
        {:else if request.solution.part}
          <span class="text-[13px] font-medium">Finished</span>
        {:else}
          <button on:click={openDiagnosticDialog} class="secondary !text-[13px] !py-2 !px-3">Diagnostics</button>
        {/if}
      {:else}
        <XIcon class="h-4 w-4 text-red-500" />
      {/if}
    </div>
  </div>
</div>
