<script>
  import dayjs from 'dayjs'
  import DiagnosticsCard from './DiagnosticsCard.svelte'
  import DiagnosticsSolution from './DiagnosticsSolution.svelte'
  import { diagnostics, resetDiagnostics, simulation } from '$lib/api/requests'
  import { openDialog, openNotification } from '$lib/stores/appStore'
  import AlarmDialog from './AlarmDialog.svelte'

  export let close
  export let request

  const { make, model, registration } = request.vehicle

  let diagnosticsCard

  let started = false
  let finished = false
  let loading = false
  let simStarted = false
  let solutionNotFound = false

  let measurement
  let inputValue = null

  const submit = async () => {
    if (started) {
      diagnosticsCard?.trySubmit()
    } else {
      await runDiagnostics()
    }
  }

  const runDiagnostics = async () => {
    loading = true
    measurement = await diagnostics(request.id, inputValue)
    inputValue = null
    started = true
    finished = !!measurement.solution
    solutionNotFound = measurement?.type === 'not-found'
    if (finished && measurement.alarm) {
      const part = measurement.alarm.part
      openNotification(
        `The part "${part.make} - ${part.name}" is running low. You might want to order a new one.`, 
        'Part Quantity Notification'
      )
    }
    loading = false
  }

  const reset = async () => {
    loading = true
    await resetDiagnostics(request.id)
    inputValue = null
    measurement = null
    started = false
    finished = false
    solutionNotFound = false
    setTimeout(() => loading = false, 200)
  }

  const closeDialog = async () => {
    !finished && await reset()
    close(finished ? 'ok' : 'cancel')
  }

  const simulateAlarm = async () => {
    simStarted = true
    await simulation(request.id, 'start')
    setTimeout(async () => {
      const alarm = await simulation(request.id, 'status')
      alarm.vehicleId && openDialog(AlarmDialog, { alarm }, async result => {
        simulation(request.id, 'reset')
        close()
      })
    }, 13000)
  }

  const resetSimulation = async () => {
    await simulation(request.id, 'reset')
  }

</script>

<div class="w-[420px]">
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
          {#if !simStarted}
            <button on:click={simulateAlarm} class="underline text-neutral-600 text-[13px] p-0 bg-transparent">
              Start Alarm Simulation?
            </button>
          {:else}
            <button on:click={resetSimulation} class="underline text-neutral-600 text-[13px] p-0 bg-transparent">
              Reset Simulation?
            </button>
          {/if}
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

  {#if solutionNotFound}
    <div class="mt-10">
      <span class="text-sm">No solution found for this problem.</span>
    </div>
  {:else if loading}
    <div class="mt-10">
      <span>Loading...</span>
    </div>
  {:else if started && !finished}
    <DiagnosticsCard on:submit={runDiagnostics} bind:this={diagnosticsCard} bind:value={inputValue} {measurement} />
  {:else if finished}
    <DiagnosticsSolution solution={measurement.solution} />
  {/if}

  <div class="mt-14 flex">
    <button on:click={reset} class="bg-transparent text-sm px-0 py-2.5 hover:text-neutral-600">Reset diagnostics</button>
    <button on:click={closeDialog} class="secondary py-2.5 !text-sm ml-auto">{finished ? 'Finish' : 'Cancel'}</button>
    {#if !finished}
      <button on:click={submit} class="primary ml-2 px-8 py-2.5 !text-sm">
        {started ? 'Continue' : 'Start'}
      </button>
    {/if}
  </div>
</div>
