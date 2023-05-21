<script>
  import dayjs from 'dayjs'
  import DiagnosticsCard from './DiagnosticsCard.svelte'
  import DiagnosticsSolution from './DiagnosticsSolution.svelte'
  import { diagnostics, resetDiagnostics } from '$lib/api/requests'

  export let close
  export let request

  const { make, model, registration } = request.vehicle

  let started = false
  let finished = false

  let measurement
  let inputValue = null

  const runDiagnostics = async () => {
    measurement = await diagnostics(request.id, inputValue)
    console.log(measurement)
    started = true
    finished = !!measurement.solution
  }

  const reset = async () => {
    await resetDiagnostics(request.id)
    inputValue = null
    measurement = null
    started = false
    finished = false
  }

  const closeDialog = async () => {
    !finished && await reset()
    close(finished ? 'ok' : 'cancel')
  }
</script>

<div>
  <h3>Diagnostics Process</h3>
  <div class="mt-5">
    <div class="flex items-center gap-10">
      <div class="flex items-center">
        <img
          src={`images/${make.toLowerCase()}.jpg`}
          alt="Make car"
          class="-ml-4 h-40 w-60 object-contain"
        />
        <div>
          <span class="block font-medium">{make}</span>
          <span class="block text-[13px]">{model}</span>
          <span class="block text-[13px]">{registration}</span>
        </div>
      </div>
    </div>
  </div>
  <div class="text-left">
    <span class="block text-sm font-semibold">Reported problem</span>
    <span class="text-sm">{request.problem.description}</span>
    {#if request.scheduledAt !== null}
      <div class="text-[13px]">
        Scheduled at {dayjs(request.scheduledAt).format('MMMM D, YYYY h:mm A')}
      </div>
    {/if}
  </div>

  {#if started && !finished}
    <DiagnosticsCard bind:value={inputValue} {measurement} />
  {:else if finished}
    <DiagnosticsSolution solution={measurement.solution} />
  {/if}

  <div class="mt-8 flex">
    <button on:click={reset} class="bg-transparent px-0 py-3">Reset diagnostics</button>
    <button on:click={closeDialog} class="secondary ml-auto">{finished ? 'Finish' : 'Cancel'}</button>
    {#if !finished}
      <button on:click={runDiagnostics} class="primary ml-2 px-8">
        {started ? 'Continue' : 'Start'}
      </button>
    {/if}
  </div>
</div>
