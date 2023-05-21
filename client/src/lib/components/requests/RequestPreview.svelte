<script>
  import ClockIcon from '~icons/tabler/clock-filled'
  import CheckIcon from '~icons/tabler/circle-check-filled'
  import XIcon from '~icons/tabler/circle-x-filled'

  import { isCustomer } from '$lib/stores/userStore'

  export let request

  const { make, model, registration } = request.vehicle

  const requestStatus = request.accepted ? 'Accepted' : request.rejected ? 'Rejected' : 'Pending'
</script>

<div class="h-30 w-full rounded-xl border border-neutral-300 bg-white p-5">
  <div class="flex items-center justify-between">
    <div class="flex gap-10 items-center">
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
      </div>
    </div>

    <div class="flex items-center gap-1.5">
      {#if requestStatus !== 'Pending' || $isCustomer}
        <span class="text-[13px] font-medium">{requestStatus}</span>
      {/if}
      {#if requestStatus === 'Pending'}
        {#if $isCustomer}
          <ClockIcon class="h-4 w-4 text-amber-500" />
        {:else}
          <button class="secondary">Process</button>
        {/if}
      {:else if requestStatus === 'Accepted'}
        <CheckIcon class="h-4 w-4" />
      {:else}
        <XIcon class="h-4 w-4 text-red-500" />
      {/if}
    </div>
  </div>
</div>
